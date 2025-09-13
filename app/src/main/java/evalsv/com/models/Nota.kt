package evalsv.com.models

data class Nota(
    var id: String? = "",
    var estudianteId: String? = "",
    var nombreEstudiante: String? = "",
    var grado: String? = "",
    var materia: String? = "",
    var notaFinal: Double? = 0.0,
) {
    constructor() : this("", "", "", "", "", 0.0)
}