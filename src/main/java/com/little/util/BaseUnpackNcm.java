package com.little.util;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.datatype.Artwork;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author created by qingchuan.xia
 */
abstract class BaseUnpackNcm {

    RepeatReadStream stream = null;
    protected static int byte2intLength = 4;
    protected static FileIterator fileIterator = new DefaultFileIterator();
    protected byte[] coreKey = ByteUtil.hexStr2Byte("687A4852416D736F356B496E62617857");
    protected byte[] metaKey = ByteUtil.hexStr2Byte("2331346C6A6B5F215C5D2630553C2728");
    protected byte[] headerKey = ByteUtil.hexStr2Byte("4354454e4644414d");


    void ncm2NormalFormat(File file) throws TagException, ReadOnlyFileException, CannotReadException, InvalidAudioFrameException, IOException, CannotWriteException {

        try {
            //文件头有8位header 2位空byte
            stream.setFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        stream.skip(10);
        //音乐段byte的密码
        byte[] keyData = readKey();
        //音乐文件的mete字符串
        String meteData = readMetaData();
        int crc32 = readCrc32();
        byte[] cover = readAlbumCover();
        byte[] musicData = readMusic(keyData);
        MusicInfo musicInfo = getInfo(file.getParent(), meteData);
        File saveFile = new File(file.getAbsolutePath().replace(".ncm", "." + musicInfo.getFormat()));
        write(saveFile, musicData);
        AudioFile music = AudioFileIO.read(saveFile);
        Tag tag = music.getTag();
        tag.deleteArtworkField();
        setMusicTag(tag, musicInfo);
        Artwork artwork = new Artwork();
        setCover(artwork, cover);
        tag.setField(artwork);
        music.commit();
        //System.out.println(saveFile.toString());
    }

    /**
     * 读取解密秘钥信息
     */
    abstract byte[] readKey() throws IOException;

    abstract String readMetaData() throws IOException;

    abstract int readCrc32() throws IOException;

    abstract byte[] readAlbumCover() throws IOException;

    abstract byte[] readMusic(byte[] keyByte);

    abstract MusicInfo getInfo(String parentPath, String meteData);

    abstract void write(File target, byte[] data);

    abstract void setMusicTag(Tag tag, MusicInfo musicInfo) throws FieldDataInvalidException;

    private void setCover(Artwork artwork, byte[] cover) {
        artwork.setBinaryData(cover);
        artwork.setMimeType("image/jpeg");
        artwork.setPictureType(3);
    }


}
