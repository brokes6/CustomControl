# CustomControl
[![](https://jitpack.io/v/brokes6/CustomControl.svg)](https://jitpack.io/#brokes6/CustomControl)<br>
个人制作的自定义控件库<br>
包含了一些常用的控件<br>
如何使用：<br>
Step 1. Add the JitPack repository to your build file<br>
```Java
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2. Add the dependency<br>
```Java
implementation 'com.github.brokes6:CustomControl:v1.0.2'
```

效果如下:<br>
IntPutNumRelativeLayout:<br>
![image](https://github.com/brokes6/CustomControl/blob/master/CustomControlLibs/src/showresources/IntPutNumRelativeLayout.gif)<br>
属性如下:<br>
```Java
 <attr name="max" format="integer"/>
 <attr name="min" format="integer"/>
 <attr name="step" format="integer"/>
 <attr name="disable" format="boolean"/>
```
SildeMenuView:<br>
效果如下:<br>
![image](https://github.com/brokes6/CustomControl/blob/master/CustomControlLibs/src/showresources/SildeMenuView.gif)<br>
属性如下:<br>
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
