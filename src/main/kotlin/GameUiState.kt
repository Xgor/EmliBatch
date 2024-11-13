data class GameUiState (
    var folderPath: String = "Select Folder",
    var filterText: String = "",
    val sortInvert: Boolean = false,
    val canRename: Boolean = false,
    val fileList: List<FileName> = listOf<FileName>(),
    val commandList: List<commandLayer> = listOf<commandLayer>(),
    var sortType: String = "Name",
)