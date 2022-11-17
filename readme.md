# Security
1. [main](https://github.com/godfreynolan/jetpacks)
2. [jetpack/view-binding](https://github.com/godfreynolan/jetpacks/tree/jetpack/view-binding)
3. [jetpack/card-view](https://github.com/godfreynolan/jetpacks/tree/jetpack/card-view)
4. [jetpack/lifecycle](https://github.com/godfreynolan/jetpacks/tree/jetpack/lifecycle)
5. **jetpack/security**
6. [jetpack/benchmark](https://github.com/godfreynolan/jetpacks/tree/jetpack/benchmark)
7. [jetpack/navigation](https://github.com/godfreynolan/jetpacks/tree/jetpack/navigation)
8. [jetpack/hilt](https://github.com/godfreynolan/jetpacks/tree/jetpack/hilt)
9. [jetpack/room](https://github.com/godfreynolan/jetpacks/tree/jetpack/room)
10. [jetpack/compose](https://github.com/godfreynolan/jetpacks/tree/jetpack/compose)
## Setup
Jetpack Security was added to this project to illustrate how data can be stored securely to `SharedPreferences` through the use of the `EcnryptedSharedPreferences` class. To add it to the project, the following dependency was added to the `app` level `build.gradle`
> Note: `GSON` was also added to allow objects to be directly converted to strings that can be saved to `EncryptedSharedPreferences`
```gradle
// Security
implementation "androidx.security:security-crypto:1.0.0"
// JSON
implementation 'com.google.code.gson:gson:2.10'
```

## Encrypted Shared Preferences
A new class called `EncryptedSharedPrefsHelper.kt` was created and placed in a new package called `security`
```kotlin
class EncryptedSharedPrefsHelper(private val context: Context) {

}
```
> Context will need to be passed to this class to initialize an instance of `EncryptedSharedPreferences`

`EncryptedSharedPreferences` will be instantiated as the class-level variable `sharedPrefs`
```kotlin
class EncryptedSharedPrefsHelper(private val context: Context) {

  companion object {
        private const val TAG = "EncryptedSharedPrefs"
        private const val FILE_NAME = "jetpack_eta_encrypted_shared_prefs"
    }

  private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
  private val sharedPrefs = EncryptedSharedPreferences.create(
      FILE_NAME,
      masterKeyAlias,
      context,
      EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
      EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
  )
}
```

Next, helper functions will be used to convert `class instances -> JSON -> strings` so that information in classes can be stored directly into shared preferences. Then, a another function will be responsible for converting the data from `string -> JSON -> class instance`
```kotlin
class EncryptedSharedPrefsHelper(private val context: Context) {

  ...

  /**
  * Saves an Object to Encrypted Shared Preferences
  */
  fun <T> savePreference(key: String, value: T) {
      val gson = Gson()
      val save = gson.toJson(value)
      savePreference(key, save)
  }

  /**
    * Saves a string to Encrypted Shared Preferences
    */
  private fun savePreference(key: String, value: String): Boolean {
      return sharedPrefs
          .edit()
          .putString(key, value)
          .commit()
  }

  fun <T> getPreference(key: String, ofClass: Type): T? {
      val retrieved = sharedPrefs.getString(key, "") ?: return null
      return Gson().fromJson(retrieved, ofClass)
  }
}
```

## View Model
Now, an instance of the `EncryptedSharedPrefsHelper` can be instantiated by the `ViewModel` and used to store information securely. In this project, it will be used to store a user's favorite routes.

In the `StopsViewModel`, the encrypted shared preferences was instantiated and functions were created to retrieve, favorite, and unfavorite routes
```kotlin
class StopsViewModel(application: Application) : AndroidViewModel(application) {

  ...

  private val encryptedSharedPrefs = EncryptedSharedPrefsHelper(application.applicationContext)

  ...

  fun favorite(position: Int) {
    viewModelScope.launch(Dispatchers.IO) {
        val tempStops = stops.value?.toMutableList() ?: mutableListOf()
        tempStops[position].favorite = true
        val favorites = getFavorites()
        favorites.add(tempStops[position])
        encryptedSharedPrefs.savePreference(FAVORITE_STOPS, favorites)
        withContext(Dispatchers.Main) {
            stops.postValue(tempStops)
        }
    }
  }

  fun removeFavorite(stop: StopUi, position: Int) {
    viewModelScope.launch(Dispatchers.IO) {
        val tempStops = stops.value?.toMutableList() ?: mutableListOf()
        tempStops[position].favorite = false
        val favorites = getFavorites()
        favorites.removeIf { it.stopId == stop.stopId && it.stopName == stop.stopName }
        encryptedSharedPrefs.savePreference(FAVORITE_STOPS, favorites)

        withContext(Dispatchers.Main) {
            stops.postValue(tempStops)
        }
    }
  }

  private fun getFavorites(): MutableList<StopUi> {
    val typeToken = object : TypeToken<MutableList<StopUi>>() {}.type
    return encryptedSharedPrefs.getPreference(FAVORITE_STOPS, typeToken) ?: mutableListOf()
  }
}
```

## Fragment / Recycler Adapter
In the `StopsFragment`, the favorite / unfavorite functions can be called when the favorite button is clicked on a stop. In the `recycler_view_item.xml`, a `star` icon was added.
```xml
  ...

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

    <ImageView
        android:id="@+id/favoriteImageView"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        android:layout_margin="5dp"
        android:layout_width="36dp"
        android:layout_height="36dp"
        android:visibility="gone"
        android:clickable="true"
        android:focusable="true"
        android:src="@drawable/ic_baseline_star_outline_24"
        app:tint="@color/purple_700" />

  </androidx.constraintlayout.widget.ConstraintLayout>

  ...

```

Using callbacks, when the `star` icon is clicked, the click event can be propagated from the `adapter` to the `fragment`. Then, the fragment can invoke the favorite or unfavorite call to the `view model`. In the `StopFragment`'s `onCreateView`, the following code was added
```kotlin
 ...

  adapter = StopRecyclerAdapter(stops).apply {
    onItemClicked = { stop, isFavorite, position ->
        if(isFavorite) viewModel.favorite(position)
        else viewModel.removeFavorite(stop, position)
    }
  }

 ...
```

In the `StopRecyclerAdapter`, the callback can be invoked when the `star` icon is clicked. When clicked, the `StopUI` instance, the position in the recycler adapter, and if it should be favorited or unfavorited will be supplied to the callback
```kotlin
typealias OnItemClicked = (StopUi, Boolean, Int) -> Unit

class StopRecyclerAdapter(private val stops: List<StopUi>): RecyclerView.Adapter<StopRecyclerAdapter.ViewHolder>() {

  ...

  var onItemClicked: OnItemClicked? = null

  // Binds the `Company` data to the elements in the RecyclerView Items
  inner class ViewHolder(private val binding: RecyclerViewItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(stop: StopUi, position: Int) {
        binding.name.text = stop.stopName
        binding.favoriteImageView.visibility = View.VISIBLE
        binding.favoriteImageView.setImageResource(R.drawable.ic_baseline_star_outline_24)
        if(stop.favorite) binding.favoriteImageView.setImageResource(R.drawable.ic_baseline_star_24)

        binding.favoriteImageView.setOnClickListener { onItemClicked?.invoke(stop, !stop.favorite, position) }
    }
  }

  ...

}

```