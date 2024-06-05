interface NameRepository {
    fun getName(): String
}

class NameRepositoryImpl() : NameRepository {
    override fun getName(): String {
        return "Kotlin"
    }
}

// Example showcasing constructor injection

class NameRetrievalHelper(private val nameRepository: NameRepository) {
    fun getNameFromRepo() = nameRepository.getName()
}


// Example showcasing setter injection
class NameRetrievalSetterHelper() {
    private var nameRepository: NameRepository? = null

    fun setNameRepository(nameRepository: NameRepository) {
        this.nameRepository = nameRepository
    }

    fun getNameFromRepo(): String? {
        return nameRepository?.getName()
    }
}

// Example showcasing method injection

class NameRetrievalMethodHelper {
    fun getNameFromRepo(nameRepository: NameRepository): String {
        return nameRepository.getName()
    }
}


fun main() {
    //triggering constructor injection
    val nameRepository = NameRepositoryImpl()
    val nameRetrievalHelper = NameRetrievalHelper(nameRepository)

    println(nameRetrievalHelper.getNameFromRepo())

    // triggering setter injection

    val nameRetrievalSetterHelper = NameRetrievalSetterHelper()
    nameRetrievalSetterHelper.setNameRepository(nameRepository)
    nameRetrievalSetterHelper.getNameFromRepo()?.let {
        println(it)
    }

    // triggering method injection
    val nameRetrievalMethodHelper = NameRetrievalMethodHelper()
    val name = nameRetrievalMethodHelper.getNameFromRepo(nameRepository)
    println(name)

}

