# CustomControl
[![](https://jitpack.io/v/brokes6/CustomControl.svg)](https://jitpack.io/#brokes6/CustomControl)<br>
个人制作的自定义控件库<br>
包含了一些常用的控件<br>
如何使用：<br>
## Step 1. Add the JitPack repository to your build file<br>
```Java
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
## Step 2. Add the dependency<br>
```Java
implementation 'com.github.brokes6:CustomControl:v1.0.2'
```
# IntPutNumRelativeLayout:<br>
## 效果如下:<br>
![image](https://github.com/brokes6/CustomControl/blob/master/CustomControlLibs/src/showresources/IntPutNumRelativeLayout.gif)<br>
## 属性如下:<br>
```Java
 <attr name="max" format="integer"/>
 <attr name="min" format="integer"/>
 <attr name="step" format="integer"/>
 <attr name="disable" format="boolean"/>
```
# SildeMenuView:<br>
## 效果如下:<br>
![image](https://github.com/brokes6/CustomControl/blob/master/CustomControlLibs/src/showresources/SildeMenuView.gif)<br>
## 属性如下:<br>
课包括一个子类，子类右滑自带 已读，置顶，删除。<br>
```Java
 <com.example.customcontrollibs.SildeMenuView
        android:id="@+id/sildeMenu_View"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="70dp"
            android:background="#fff"
            android:gravity="center"
            android:text="测试文字"
            android:textSize="20sp" />
    </com.example.customcontrollibs.SildeMenuView>
```
可替换编辑区布局<br>
```Java
 public void setEditView(View v)
```
# TopTextView:<br>
## 效果如下:<br>
![image](https://github.com/brokes6/CustomControl/blob/master/CustomControlLibs/src/showresources/Image4.jpg)<br>
## 属性如下:<br>
上面数字加下面文字（上下垂直排版）<br>
```Java
 	<attr name="Top_Text" format="string" />
        <attr name="Bottom_Text" format="string" />
        <attr name="Text_Spacing" format="integer" />
        <attr name="Top_Text_Color" format="color" />
        <attr name="Bottom_Text_Color" format="color" />
        <attr name="Top_Text_Size" format="dimension" />
        <attr name="Bottom_Text_Size" format="dimension" />
```
## 使用如下:<br>
```Java
 <com.example.customcontrollibs.TopTextView
            android:layout_width="80dp"
            android:layout_height="80dp"
            app:Bottom_Text="动态"
            app:Text_Spacing="20"
            app:Top_Text="36" />
```
# ImageTopView:<br>
## 效果如下:<br>
![image](https://github.com/brokes6/CustomControl/blob/master/CustomControlLibs/src/showresources/image5.jpg)<br>
## 属性如下:<br>
```Java
 	<attr name="Image_Text_Spacing" format="integer"/>
        <attr name="Text_Size" format="dimension"/>
        <attr name="Image_Text" format="string"/>
        <attr name="Drawable" format="reference|color"/>
        <attr name="Image_Size" format="integer"/>
        <attr name="Text_Color" format="reference|color"/>
	<attr name="Is_radius" format="boolean"/>
```
## 使用如下:<br>
```Java
<com.example.customcontrollibs.ImageTopView
            android:layout_width="80dp"
            android:layout_height="80dp"
	    app:Drawable="@mipmap/offline_caching"
            app:Image_Text="更多"
            app:Image_Text_Spacing="20"
            app:Text_Color="#F10000" />
```
# CustomChannelView:<br>
## 效果如下:<br>
![image](https://github.com/brokes6/CustomControl/blob/master/CustomControlLibs/src/showresources/image6.jpg)<br>
## 属性如下:<br>
```Java
 	<attr name="Ch_TopUrl" format="string"/>
        <attr name="Ch_Bottom_Text" format="string"/>
        <attr name="Ch_Bottom_Time" format="string"/>
        <attr name="Ch_middle_Url" format="string"/>
        <attr name="Ch_ImageRadius" format="integer"/>
```
## 使用如下:<br>
```Java
<com.example.customcontrollibs.CustomChannelView
            android:layout_width="140dp"
            android:layout_height="200dp"
            app:Ch_Bottom_Text="起飞~"
            app:Ch_Bottom_Time="08-10浏览"
            android:elevation="6dp"/>
	    
	 //在activity/fragment中设置url
	item_two = findViewById(R.id.item_two);
        item_two.setTopUrl("https://i0.hdslb.com/bfs/archive/c1e48e6aaf5e2eb430de9e9c635cb626103c0bef.jpg@412w_232h_1c_100q.jpg");
	//若没有设置TopUrl，则会默认设置一个纯色背景
        item_two.setMiddleUrl("https://i1.hdslb.com/bfs/face/acc7a0e97bf9f6c4d047777e40270a39bc7f4f7d.jpg");
```
## 使用说明:<br>
组合使用了以下<br>
implementation 'com.gcssloop.widget:rclayout:1.8.1'( https://github.com/GcsSloop/rclayout )<br>
implementation 'de.hdodenhof:circleimageview:3.0.0'( https://github.com/hdodenhof/CircleImageView )
