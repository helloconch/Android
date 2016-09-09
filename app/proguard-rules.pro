# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/SoftWare/Android/adt-bundle-mac-x86_64-20140702/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#代码混淆压缩比，在0~7之间
-optimizationpasses 5
#混淆时不使用大小写混合，混淆后的类名为小写
-dontusemixedcaseclassnames
#指定不去忽略非公共的库的类
-dontskipnonpubliclibraryclasses
#指定不去忽略非公共的库的类的成员
-dontskipnonpubliclibraryclassmembers
#不做预校验,preverify，Android不需要preverify，加快混淆速度
-dontpreverify
#verbose，混淆后生成映射文件，包含有类名->混淆后类名的映射关系，使用printmapping指定映射文件的名称
-verbose
-printmapping proguardMapping.txt
#指定混淆时采用的算法，后面的参数是一个过滤器
#这个过滤器是谷歌推荐的算法，一般不会变
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#保护代码中的Annotation不被混淆
#这在JSON实体映射时非常重要，比如fastJson
-keepattributes *Annotation*
#避免混淆泛型
#这在JSON实体映射时非常重要，比如fastJson
-keepattributes Signature
#抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

#保留所有本地native方法不被混淆
-keepclasseswithmembernames  class * {
    native <methods>;
}

#保留继承自Activity、Application这些子类
-keep public class * extends android.app.Activity


