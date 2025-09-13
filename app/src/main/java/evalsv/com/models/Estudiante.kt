package evalsv.com.models

data class Estudiante(
    var id: String? = null,
    val nombreCompleto: String? = null,
    val edad: Int? = null,
    val direccion : String? = null,
    val celular : String? = null
) {
    constructor() : this(null, null, null, null, null)
}