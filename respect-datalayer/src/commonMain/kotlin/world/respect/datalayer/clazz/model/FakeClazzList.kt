package world.respect.datalayer.clazz.model

data class FakeClazzList(
    val clazzName: String,
    val clazzFilter :List<FakeFilter> = emptyList(),

    )

data class FakeFilter(
    val filterName: String
)