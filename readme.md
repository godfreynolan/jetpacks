# Card View
1. [main](https://github.com/godfreynolan/jetpacks)
2. [jetpack/view-binding](https://github.com/godfreynolan/jetpacks/tree/jetpack/view-binding)
3. **jetpack/card-view**
4. [jetpack/lifecycle](https://github.com/godfreynolan/jetpacks/tree/jetpack/lifecycle)
5. [jetpack/security](https://github.com/godfreynolan/jetpacks/tree/jetpack/security)
6. [jetpack/benchmark](https://github.com/godfreynolan/jetpacks/tree/jetpack/benchmark)
7. [jetpack/navigation](https://github.com/godfreynolan/jetpacks/tree/jetpack/navigation)
8. [jetpack/hilt](https://github.com/godfreynolan/jetpacks/tree/jetpack/hilt)
9. [jetpack/room](https://github.com/godfreynolan/jetpacks/tree/jetpack/room)
10. [jetpack/compose](https://github.com/godfreynolan/jetpacks/tree/jetpack/compose)
## Setup
As part of `implementation 'com.google.android.material:material:1.7.0'` in the `app` level `build.gradle`, card view can be used in layout files. In this project, `CardView` was utilized in the different recycler views for companies, routes, and stops. It contains the text to be displayed and will house any additional elements in the future. In the `recycler_view_item.xml` file, the following code repalaced the singular `TextView`
```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.cardview.widget.CardView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_marginStart="10dp"
    android:layout_marginEnd="10dp"
    android:layout_marginTop="5dp"
    android:layout_marginBottom="5dp"
    app:cardCornerRadius="5dp"
    app:cardElevation="4dp"
    android:clickable="true"
    android:focusable="true">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <TextView
            android:id="@+id/name"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintBottom_toBottomOf="parent"
            tools:text="SMART"
            android:textSize="20sp"
            style="@style/TextAppearance.AppCompat.Headline"
            android:paddingStart="10dp"
            android:paddingTop="5dp"
            android:paddingBottom="5dp"
            android:paddingEnd="10dp" />

    </androidx.constraintlayout.widget.ConstraintLayout>

</androidx.cardview.widget.CardView>
```