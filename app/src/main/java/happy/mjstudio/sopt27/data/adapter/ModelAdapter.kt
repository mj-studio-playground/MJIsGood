package happy.mjstudio.sopt27.data.adapter

interface ModelAdapter<D, E> {
    fun toEntity(source: D): E
    fun toDTO(source: E): D
}