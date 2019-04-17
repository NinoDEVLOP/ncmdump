### JAVA版转换网易云ncm文件的项目

#### 感谢

- [nondanee/ncmdump](https://github.com/nondanee/ncmdump)：我作为参考的python项目

- [anonymous5l/ncmdump](https://github.com/anonymous5l/ncmdump)：python项目参考的原始C++项目

#### 使用说明

​    将项目按照maven项目导入ide中，使用maven：```package -f pom.xml``` 在工程目录的targer目录下,文件名包含```jar-with-dependencies```的那个jar包才是正常能够运行的jar，在命令行中使用```java -jar <jarFile> <filePath>```,示例:

```
java -jar num_util-jar-with-dependencies.jar C:\music
#当然也可以直接转换ncm文件
java -jar num_util-jar-with-dependencies.jar C:\music\test.ncm

```

​    会在.ncm文件所在位置生成可以直接播放的正常格式的音乐文件。

ps: 使用前请提前安装好正常的jdk环境