# Lifecycle
## Setup
With the `lifecycle` jetpack, `ViewModels` and `LiveData` can be used to manage a fragments state outside of it's normal lifecycle. To add it to a project, the following dependencies need to be added to the `app` level `build.gradle`
> Note: Kotlin coroutines were also added to take advantage of easy asyncronous calls and further demonstrate the use of `LiveData`
```gradle
// Lifecycle
def lifecycle_version = "2.5.1"
implementation "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"
implementation "androidx.fragment:fragment-ktx:1.5.4"
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1'
```
## ViewModels
Each fragment created in this project recieved its own `AndroidViewModel`. This type of view model behaves similar to `ViewModel` but provides `ApplicationContext`. This allows for the sqlite database to be instantiated directly in the view model.

The following code was added to a new file called `CompaniesViewModel.kt` to create a new instance of an `AndroidViewModel`:
```kotlin
class CompaniesViewModel(application: Application) : AndroidViewModel(application) {
 // Todo
}
```

Next, the corresponding fragment `CompaniesFragment` was updated to get an instance of the view model. The following class-level variable `viewModel` was added
```kotlin

class CompaniesFragment: Fragment() {

    companion object {
        const val TAG = "CompaniesFragment"
    }

    private var _binding: FragmentCompanyBinding? = null
    private val binding get() = _binding!!

    // Add ViewModel
    private val viewModel by viewModels<CompaniesViewModel>()

    ...
}
```

Now, any method in the view model can be accessed from the fragment.

### LiveData
Since the database call can take an indeterminate amount of time, `livedata` can be used to update the fragment when the data is ready.

First, the database was initialized inside the view model
```kotlin
class CompaniesViewModel(application: Application) : AndroidViewModel(application) {

  // Initialize Connection to DB
  private val dbName = "gtfs_room.db"
  private val dbInputStream = application.applicationContext.assets.open("gtfs_room.db")
  private val helper = SqliteHelper.getInstance(dbInputStream, dbName)
}
```

Then a `MutableLiveData` class-level variable was created to hold the result of the database call.
```kotlin
class CompaniesViewModel(application: Application) : AndroidViewModel(application) {

  ...

  // Create mutable live data for the `Company` list
  val companies = MutableLiveData<List<Company>>(emptyList())
}
```

### Coroutines
A coroutine on the `IO` thread will be created when the `getCompanies` function is called. When the list is ready, the list will be posted to the `companies` live data on the Main (UI) thread.
```kotlin
class CompaniesViewModel(application: Application) : AndroidViewModel(application) {
  ...

  fun getCompanies() {
    // Launch new Coroutine for fetching the DB data
    viewModelScope.launch(Dispatchers.IO) {
        val newCompanies = helper.getCompanies()

        // On the `Main` Thread, post the new companies
        withContext(Dispatchers.Main) {
            companies.postValue(newCompanies)
        }
    }

  }

}

```

## Fragments
In the fragment, the companies function will need to be called. In the `onCreateView`, the call to the `getCompanies` view model function was called
```kotlin
override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
): View {
    _binding = FragmentCompanyBinding.inflate(inflater, container, false)

    // Fetch the company list
    viewModel.getCompanies()

    ...

}
```

### Observers
Now that the data is being fetched, it needs to now update the recycler view list on the UI. To do so, an observer was set up to listen for changes to the `companies` live data from the fragment. Whenever the data changes, the observer will be notified with the new value.

An `Observer` was added as a class-level variable to the fragment that clears the current list of the recycler adapter, adds all of the items from the newly fetched data, and notifies the adapter of the data change.
```kotlin
class CompaniesFragment: Fragment() {

  ...

  // Create an Observer that will automatically update
  // the recycler view when the data changes
  @SuppressLint("NotifyDataSetChanged")
  private val companyListObserver = Observer<List<Company>> {
      companies.clear()
      companies.addAll(it)
      adapter.notifyDataSetChanged()
  }

  ...

}
```
Now that the observer will update the adapter, it just needs to subscribe to the live data state changes. It will also need to be unsubscribed when the fragment is stopped. Therefore, the fragment's `onResume` and `onStop` functions will be used for the subscribing and unsubscribing, respectively.
```kotlin
override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
): View {

  ...

  override fun onResume() {
    super.onResume()
    // When fragment resumes, start observer
    viewModel.companies.observe(this, companyListObserver)
  }

  override fun onStop() {
    super.onStop()
    // When fragment stops, remove the live data observer
    viewModel.companies.removeObserver(companyListObserver)
  }

}
```
> Note: The code examples describe the changes made to the `companies` feature to support the `lifecycle` jetpack, however, these changes were also made to the `routes` and `stops` features.