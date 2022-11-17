# View Binding

## Setup
To add `View Binding` to a project, the `viewBindin` build feature was added to the `app` level `build.gradle`
```gradle
android {
  ...
  buildFeatures {
        viewBinding true
    }
}
```

## Main Activity
Now that view binding is enabled, direct calls to xml elements with `findViewById` can be replaced with `View Binding`. In `MainActivity.kt`, a variable to reference the binding will need to be created
```kotlin
private lateinit var binding: ActivityMainBinding
```

> The binding follows the name of the layout that should be bound, therefore, `main_activity.xml` is referenced as `MainActivityBinding`

 Inside the `onCreate` function, the binding can be initialized. In addition, the content view can be set to the binding's root. All elements in the layout can now be accessed through `binding.<element_name>`
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  binding = ActivityMainBinding.inflate(layoutInflater)

  binding = ActivityMainBinding.inflate(layoutInflater)
  setContentView(binding.root)
  setSupportActionBar(binding.toolbar)
  ...
}
```

## Fragments
Adding `View Binding` to fragments is very similar to activities. Following [Android documentation](https://developer.android.com/topic/libraries/view-binding), the fragment should have two class-level variables
```kotlin
private var _binding: FragmentCompanyBinding? = null
private val binding get() = _binding!!
```
where `_binding` is of type `FragmentCompanyBinding`or the equivalent name for a different xml file. In this case, `fragment_company.xml` is being bound.

In the fragment's `onCreateView`, `_binding` will be inflated similar to that of the activity. Then, `binding` will be used to access the elements contained in the layout.
```kotlin
override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
): View {
  
  _binding = FragmentCompanyBinding.inflate(inflater, container, false)

  ...
}
```

## Recycler Adapters
For recycler adapters, the `Recycler Adapter` class and its subsequent `ViewHolder` will need to be updated. In the `onCreateViewHolder` function of the adapter, it should inflate the binding and return a `ViewHolder` with the `binding` as an argument. This will inflate the `recycler_view_item.xml` for every item in the recycler view.
```kotlin
override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
  val binding = RecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
  return ViewHolder(binding)
}
```

Next, the `ViewHolder` is updated to accept the binding. Now the binding can be used to access individual ui elements
```kotlin
inner class ViewHolder(private val binding: RecyclerViewItemBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(company: Company) {
        binding.name.text = company.name
        binding.name.setOnClickListener { onItemClicked?.invoke(company) }
    }
}
```