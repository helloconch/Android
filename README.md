# Android
```
```
######butterknife
```
1.根/build.gradle
 classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
2.app/build.gradle
apply plugin: 'android-apt'
dependencies {
//    butterknife
    compile 'com.jakewharton:butterknife:8.2.1'
    apt 'com.jakewharton:butterknife-compiler:8.2.1'
}

```

######multidex
```
1.app/build.gradle
dependencies {
   //multidex依赖
    compile 'com.android.support:multidex:1.0.0'
}
2. defaultConfig {
          // enable multidex support
          multiDexEnabled true
      }
3.public class MyApplication extends Application {
  
  
      /**
       * 该方法在onCreate之前调用
       *
       * @param base
       */
      @Override
      protected void attachBaseContext(Context base) {
          super.attachBaseContext(base);
          //在代码中加入multidex功能
          MultiDex.install(this);
          //第二种方案，让应用的Application继承MultiDexApplication
      }
}
```
######ImageLoader实现原理分析
######基础MVP
######Https
######自定义水平导航条
```
```