package com.little.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Base64;

/**
 * @author created by qingchuan.xia
 */
public class DefaultUnpackNcm extends BaseUnpackNcm {

    public DefaultUnpackNcm(int zeroCopyFlag) {
        stream = zeroCopyFlag == GlobalConfig.ZERO_COPY ? new ZerocopyRepeatReadStream() : new RepeatReadStream();
    }

    @Override
    byte[] readKey() throws IOException {
        int keyLength = readLength();
        //读取接下来制定长度的byte数据
        RepeatReadStream.ValidByte keyValidByte = stream.readFile(keyLength);
        byte[] encryptedKey = keyValidByte.getValidByte();
        for (int i = 0; i < encryptedKey.length; i++) {
            encryptedKey[i] ^= 0x64;
        }
        //使用AES工具解密
        byte[] bytes = AesUtils.decrypt(coreKey, encryptedKey);
        if (bytes == null) {
            throw new RuntimeException("无法解密");
        }
        String key = removeChar(new String(bytes), 17);
        bytes = key.getBytes();
        byte[] arr = ByteUtil.fillByteArr();
        for (int i = 0, j = 0; i < arr.length; i++) {
            j = (j + arr[i] + bytes[i % bytes.length]) & 0xFF;
            byte tmp = arr[i];
            arr[i] = arr[j];
            arr[j] = tmp;
        }
        return arr;
    }

    @Override
    String readMetaData() throws IOException {
        int meteLength = readLength();
        byte[] meteDate = stream.readFile(meteLength).getValidByte();
        for (int i = 0; i < meteDate.length; i++) {
            meteDate[i] ^= 0x63;
        }
        meteDate = Arrays.copyOfRange(meteDate, 22, meteDate.length);
        meteDate = Base64.getDecoder().decode(meteDate);
        meteDate = AesUtils.decrypt(metaKey, meteDate);
        String metaDataStr = removeChar(new String(meteDate, Charset.forName("UTF-8")), 6);
        return metaDataStr;
    }

    @Override
    int readCrc32() throws IOException {
        //具体这个Crc32有什么作用还不了解
        return readLength();
    }

    @Override
    byte[] readAlbumCover() throws IOException {
        stream.skip(5);
        //专辑封面图片未加密
        int coverLength = readLength();
        return stream.readFile(coverLength).getValidByte();
    }

    @Override
    byte[] readMusic(byte[] keyByte) {
        byte[] musicBytes = stream.readFile().getValidByte();

        byte[] resultKey = new byte[keyByte.length];
        for (int i = 0; i <= 0xFF; i++) {
            resultKey[i] = keyByte[(keyByte[i] + keyByte[(i + keyByte[i]) & 0xFF]) & 0xFF];
        }

        for (int j = 0; j < musicBytes.length; j++) {
            byte cursor = resultKey[(j + 1) % resultKey.length];
            musicBytes[j] ^= cursor;
        }
        return musicBytes;
    }

    @Override
    MusicInfo getInfo(String parentPath, String metaData) {
        System.out.println(metaData);
        JSONObject json = JSON.parseObject(metaData);
        String name = json.getString("musicName").replaceAll("/", "／");
        StringBuilder artistBuilder = new StringBuilder();
        JSONArray artists = json.getJSONArray("artist");
        for (int i = 0; i < artists.size(); i++) {
            JSONArray artist = artists.getJSONArray(i);
            artistBuilder.append(artist.getString(0)).append(",");
        }
        String artist = artistBuilder.deleteCharAt(artistBuilder.length() - 1).toString().replaceAll("/", "／");
        String album = json.getString("album");
        String format = json.getString("format");
        String filePath = parentPath + "\\" + artist + " - " + name + "." + format;
        System.out.println("File: " + filePath);
        return new MusicInfo(name, artist, album, format, filePath);
    }

    @Override
    void write(File target, byte[] data) {
        stream.outPutFile(target, data);
    }

    @Override
    void setMusicTag(Tag tag, MusicInfo musicInfo) throws FieldDataInvalidException {
        tag.deleteArtworkField();
        tag.setField(FieldKey.ARTIST, musicInfo.getArtist());
        tag.setField(FieldKey.ALBUM, musicInfo.getAlbum());
        tag.setField(FieldKey.TITLE, musicInfo.getName());
    }

    /**
     * 移除掉的是'neteasecloudmusic'字符串，但是不确定是否一定是这个，所以直接截取掉前17位
     */
    private String removeChar(String str, int from) {
        return str.trim().replaceAll("\r|\n", "").substring(from);
    }

    private int readLength() throws IOException {
        RepeatReadStream.ValidByte lengthBytes = stream.readFile(4);
        return ByteUtil.unpack(lengthBytes.getValidByte());
    }
}
