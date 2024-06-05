# MVVM & MVP

Below codes demonstrates a simple example of MVVM architecture with clean architecture. Generally
clean architecture code is having three layers.

1. UI layer :- This layer contains all the UI related code such as views, activities, fragments etc.
   Based on the architecture, whether it is MVVM or MVP, this contains viewmodels or presenters as
   well.

2. Data layer :- This layer responsible for all the data related actions such interacting with
   network or database to fetch data and modifying complex data to cater UI needs.

3. Domain layer :- Domain layer is the core layer where it will be having all the business logic.
   This layer consists of usecases, repository interfaces. Actual implementation of repositories
   will be in data layer.

```kotlin
data class Profile(val name: String, val age: Int, val email: String, val phoneNo: String)

//domain layer

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception? = null, val errorMessage: String? = null) :
        Result<Nothing>()
}

class GetProfileDetailsUseCase(private val profileRepository: ProfileRepository) {
    suspend operator fun invoke(id: Long): Flow<Result<Profile>> = profileRepository.getProfile()
}

interface ProfileRepository {
    suspend fun getProfile(id: Long): Flow<Result<Profile>>
}

// data layer
interface ProfileRemoteService {
    @GET("/getProfile")
    suspend fun getProfile(@Query id: Long): Response<Profile>
}

class ProfileRepositoryImpl(private val profileRemoteService: ProfileRemoteService) :
    ProfileRepository {
    override suspend fun getProfile(id: Long): Flow<Result<Profile>> {
        return flow {
            val response = profileRemoteService.getProfile(id)
            if (response.isSuucessfull) {
                emit(Result.Success(response.body))
            } else {
                emit(Result.Error(response.code()))
            }
        }
    }
}

//UI layer
class ProfileViewModel(private val getProfileDetailsUseCase: GetProfileDetailsUseCase) :
    ViewModel() {
    private var _profileLiveData = MutableLiveData<ProfileState>()
    val profileLiveData = _profileLiveData

    init {
        viewModelScope.launch {
            _profileLiveData.value = _profileLiveData.value?.copy(isLoading = true)
            getProfileDetailsUseCase()
                .flowOn(Dispatchers.IO)
                .catch {
                    _profileLiveData.value = _profileLiveData.value?.copy(
                        isLoading = false,
                        errorMessage = "Error"
                    )

                }.collect {
                    when (it) {
                        is Result.Success -> {
                            _profileLiveData.value = _profileLiveData.value?.copy(
                                profile = it.data,
                                isLoading = false,
                                errorMessage = ""
                            )
                        }

                        is Result.Error -> {
                            _profileLiveData.value = _profileLiveData.value?.copy(
                                profile = null,
                                isLoading = false,
                                errorMessage = it.errorMessage ?: "Error"
                            )
                        }
                    }
                }
        }
    }
}

data class ProfileState(
    val profile: Profile? = null,
    val isLoading: Boolean = false,
    error: String = ""
)

class MainActivity() : AppCompatActivity {
    private lateinit var viewModel: ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        viewModel.profileData.observe(this, Observer { profile ->
            // Update UI elements based on profile data
        })

    }

}



```

Above code displays a simple example of MVVM usage.

### Pros of MVVM ###

1. view model is responsible for all ui handling
2. views and viewmodels are loosely coupled
3. viewmodel is lifecycle aware becomes easy to handle data on configuration changes
4. in case of fragments, we can use shared viewmodels across multiple fragments which makes it easy
   to transfer data between multiple fragments
5. Viewmodels are more easier to handle unittests as it is loosely coupled with view.
6. MVVM architecture is suited for incorporating live data and data binding.

### Pros of MVP ###
1. easy to learn and implement
2. presenter is responsible for handling all ui actions
3. ideal for projects with les complexity

### Cons of MVVM ###
1. Can be more complex for simple projects

### Cons of MVP ###
1. views and presenters are tightly coupled
2. difficult to persist data on configuration changes

The choice between MVVM and MVP depends on project complexity and preferences. For complex projects
with separation of concerns and potential data sharing across fragments, MVVM is often preferred.
For simpler projects where direct UI control and a smaller codebase are desired, MVP can be a good
choice.
