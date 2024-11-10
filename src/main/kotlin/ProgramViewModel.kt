import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import javax.swing.JFileChooser
import javax.swing.JOptionPane
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
class ProgramViewModel : ViewModel() {
 //   val
    //   var filesList by remember { mutableStateOf(listOf<FileName>()) }
//    var functionList = remember { getCommandList().toMutableStateList() }


//    private val _filesList = MutableStateFlow(listOf<FileName>())
//    val filesList: StateFlow<List<FileName>> = _filesList.asStateFlow()

//    private var _functionList = mutableListOf<commandLayer>()
  //  val functionList: StateFlow<List<commandLayer>> = _functionList.asStateFlow()

  //  private val _folderPath = MutableStateFlow(String)
   // val folderPath: StateFlow<String> = _folderPath.asStateFlow()

 //   private val _folderPath = Channel<String>() // private mutable state flow
 //   val folderPath = _folderPath.receiveAsFlow() // publicly exposed as read-only state flow

  //  private val _commandList = listOf<commandLayer>()
 //   private val _filesList = listOf<FileName>()
 //   val filesList = _filesList.toMutableStateList().

 //   private val _commandList = mutableListOf<commandLayer>()
    private val _commandList = mutableListOf<commandLayer>()

 //    private val _cList = mutableLiveData<commandLayer>()

    private val _filesList = MutableStateFlow(listOf<FileName>())
    val filesList: StateFlow<List<FileName>> = _filesList.asStateFlow()
    fun GetFilesList() : StateFlow<List<FileName>>
    {
        return _filesList.asStateFlow()
    }

    private val chooser = JFileChooser()
    var folderFileList = mutableListOf<FileName>()
    // Game UI state
    private var _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()


    init {
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
    }


    fun getFileNames() = List(30) { i -> FileName(i, "Task # $i","","") }

    fun selectFolderPath(){
        val returnVal = chooser.showOpenDialog(null)
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            loadFolder(chooser.selectedFile.path)

        }
    }

    fun loadFolder(path: String)
    {
        val folder = File(path)

        folderFileList.clear()
        for ((index ,file) in folder.listFiles().asList().withIndex()){
            //          listOfFiles += fileListItem(file,true)
            folderFileList.add(FileName(index,file.name,generateFilename(file.nameWithoutExtension,_commandList,index),file.extension))
        }
        _uiState.update { currentState ->
            currentState.copy(folderPath = path, canRename = true, fileList = folderFileList)
        }
        _filesList.apply { folderFileList }
    }

    fun getFileList(path: String,commandList: List<commandLayer>): List<FileName>
    {
        val folder = File(path)
        // listOfFiles.clear()
        val fileList = mutableListOf<FileName>()
        for ((index,file) in folder.listFiles().asList().withIndex()){
            //          listOfFiles += fileListItem(file,true)
            fileList.add(FileName(0,file.name,generateFilename(file.name,commandList,index),file.extension))
        }
        return fileList.toMutableStateList()
    }

    fun changeCheckedFile(fileName: FileName,boolean: Boolean){
        fileName.enabled = boolean
        _uiState.update { currentState ->
            currentState.copy(fileList = folderFileList)
        }
        updateNewName()
    }

    fun renameFiles() {
        var index = 0
        for (fileItem in folderFileList){

            if (!fileItem.enabled)
                return
            index++
        //    fileItem.label
            val oldFilePath = Paths.get("${_uiState.value.folderPath}\\${fileItem.oldName}")
                    //    val newFileName = generateFilename(fileItem.file.name,commandList,index)

            val newFilePath = Paths.get("${_uiState.value.folderPath}\\${fileItem.newName}.${fileItem.extension}")
            val newFile = newFilePath.toFile()

            Files.move(oldFilePath, newFilePath, StandardCopyOption.REPLACE_EXISTING)
            //cleanup
            // newFile.delete()

        }
        JOptionPane.showMessageDialog(null, "Renamed file", "InfoBox: " + "Renamed files", JOptionPane.INFORMATION_MESSAGE);

        loadFolder(_uiState.value.folderPath)
        fakeUpdate()
        //  field
    }
    fun fakeUpdate()
    {
        _uiState.update { currentState ->
            currentState.copy(fileList = folderFileList.toList())
        }
    }

    fun AddCommand(textCommand: TextCommand)
    {
        _commandList.add(commandLayer(command = textCommand))
        _uiState.update { currentState ->
            currentState.copy(commandList = _commandList.toList())
        }
        updateNewName()
    }
    fun RemoveCommand(command: commandLayer)
    {

        _commandList.remove(command)
        _uiState.updateAndGet { currentState ->
            currentState.copy(commandList = _commandList.toList())
        }
      //  _uiState.updateAndGet {  }
        updateNewName()
    }

    fun updateNewName()
    {
        if (folderFileList != null && folderFileList.isEmpty()) return

        var index = 0
        for (file in folderFileList){
            if (!file.enabled) {
                file.newName = ""
                return
            }
            index++
            file.newName = generateFilename(file.oldName,_commandList,index)
        }
        _uiState.updateAndGet { currentState ->
            currentState.copy(fileList = folderFileList)
        }
    //    return fileList.toMutableStateList()
    }

    fun generateFilename(oldName: String,commands: List<commandLayer>,index: Int) : String
    {
        var name = oldName
        for (c in commands)
            name = when (c.command) {
                TextCommand.removeAll -> removeAll(name)
                TextCommand.add -> add(name,c.value1,c.value2)
                TextCommand.replace -> replace(name,c.value1,c.value2)
                TextCommand.enumerate -> enumerate(name,index,c.value2)
                TextCommand.lowercase -> lowercase(name)
                TextCommand.uppercase -> uppercase(name)
                TextCommand.removeAmount -> removeAmount(name,c.value1.toInt(),c.value2)
                TextCommand.capitalize -> capitalize(name,c.value1)
                TextCommand.removeUntil -> removeUntil(name,c.value1.toInt(),c.value2)
            }
        return name
    }


}
