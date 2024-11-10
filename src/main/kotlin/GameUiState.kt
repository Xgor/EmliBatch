data class GameUiState (
    var folderPath: String = "Select Folder",
    val currentWordCount: Int = 1,
    val score: Int = 0,
    val isGuessedWordWrong: Boolean = false,
    val canRename: Boolean = false,
    val fileList: List<FileName> = listOf<FileName>(),
    val commandList: List<commandLayer> = listOf<commandLayer>()
)