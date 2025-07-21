package world.respect.datalayer.clazz.model

class ClazzDataSource {

    fun getClazzList(): List<FakeClazzList> {
        return List(5) { index ->
            FakeClazzList(
                clazzName = "Clazz Name #$index",
                clazzFilter = listOf(
                    FakeFilter("Filter A #$index"),
                    FakeFilter("Filter B #$index")
                )
            )
        }
    }
}
