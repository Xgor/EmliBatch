import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.attribute.FileTime
import javax.swing.JFileChooser
import javax.swing.JOptionPane

class ProgramViewModel : ViewModel() {
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
    var filterFileList = mutableListOf<FileName>()
    // Game UI state
    private var _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()


    init {
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY)
    }


  //  fun getFileNames() = List(30) { i -> FileName(i, "Task # $i","","") }

    fun selectFolderPath(){
        val returnVal = chooser.showOpenDialog(null)
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            loadFolder(chooser.selectedFile.path)
        }
    }
    fun SetSortType(newSortType: String){

        _uiState.update { currentState ->
            currentState.copy(sortType = newSortType)
        }
        updateFilterField(_uiState.value.filterText)
        updateNewName()
    }

    fun InvertSorting(){
        _uiState.update { currentState ->
            currentState.copy(sortInvert = !_uiState.value.sortInvert)
        }
        updateFilterField(_uiState.value.filterText)
        updateNewName()
    }

    fun updateFilterField(string: String){

        filterFileList = mutableListOf<FileName>()
        for (file in folderFileList) {
            val name = "${lowercase(file.oldName)}.${file.extension}"
            if (name.contains(lowercase(string)))
                filterFileList.add(file)
        }
        if(_uiState.value.sortType == "File Size")
        {
            filterFileList.sortBy { it.fileSize }
        }
        if(_uiState.value.sortType == "createdTime")
        {
            filterFileList.sortBy { it.createdTime?.toMillis() }
        }
        if(_uiState.value.sortType == "modifiedTime")
        {
            filterFileList.sortBy { it.modifiedTime?.toMillis() }
        }

        if(_uiState.value.sortInvert)
            filterFileList.reverse()
        _uiState.update { currentState ->
            currentState.copy(filterText = string, fileList = filterFileList)
        }

    }

    fun loadFolder(path: String) {
        val folder = File(path)

        folderFileList.clear()
        for ((index ,file) in folder.listFiles().asList().withIndex()){
            //          listOfFiles += fileListItem(file,true)
            val path = Paths.get(file.path)
           val attributes = Files.readAttributes<BasicFileAttributes>(path, BasicFileAttributes::class.java)
//            file.las
            folderFileList.add(FileName(index,
                file.nameWithoutExtension,
                generateFilename(file.nameWithoutExtension,_commandList,index),
                file.extension,
                attributes.size(),
                attributes.lastModifiedTime(),
                attributes.lastAccessTime()))
        }

        filterFileList = folderFileList
        _uiState.update { currentState ->
            currentState.copy(folderPath = path, fileList = folderFileList)
        }

        //updateNewName()
        updateCanRename()
        _filesList.apply { folderFileList }
    }

    fun updateCanRename()
    {
        var canRename = true
        var hasEnumerate = false
        var hasRemoveAll = false
        for(c in _commandList)
        {
            if(c.command == TextCommand.removeAll) hasRemoveAll = true
            if (c.command == TextCommand.enumerate) hasEnumerate = true
        }
        if(hasRemoveAll and !hasEnumerate) canRename = false
        else
            for (file in filterFileList)
                for (file2 in filterFileList)
                    if(file != file2)
                        if(file.oldName == file2.newName)
                            canRename = false

        _uiState.update { currentState ->
            currentState.copy(canRename = canRename)
        }

    }

    fun changeCheckedFile(fileName: FileName,boolean: Boolean){
        val index = filterFileList.indexOf(fileName)
        filterFileList[index].enabled = boolean
        fileName.enabled = boolean
        _uiState.update { currentState ->
            currentState.copy(fileList = filterFileList)
        }
        updateNewName()
    }

    fun renameFiles() {
        if(!_uiState.value.canRename) return
        var index = 0
        for (fileItem in filterFileList){

            if (!fileItem.enabled)
                return
            index++
        //    fileItem.label
            val oldFilePath = Paths.get("${_uiState.value.folderPath}\\${fileItem.oldName}.${fileItem.extension}")

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
      //  var c =  folderFileList.toList()
        _uiState.update { currentState ->
            currentState.copy(fileList = listOf<FileName>())
        }
        _uiState.update { currentState ->
            currentState.copy(fileList = filterFileList)
        }
    }

    fun AddCommand(textCommand: TextCommand)
    {
        var commmand = commandLayer(command = textCommand)
        when(textCommand) {
            TextCommand.add -> commmand.value2 = "End"
            TextCommand.enumerate -> {
                commmand.value1 = "1"
                commmand.value2 = "End"
            }
            TextCommand.removeAmount -> commmand.value2 = "Start"
            TextCommand.removeUntil -> commmand.value2 = "Start"
            else -> ""
        }
        _commandList.add(commmand)
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
        updateNewName()
    }
    fun MoveCommand(command: commandLayer,move: Int)
    {
        val index = _commandList.indexOf(command)+move
        if(index < 0 || index >= _commandList.size) return
        val c = command
        _commandList.remove(command)
        _commandList.add(index,c)
        _uiState.getAndUpdate { currentState ->
            currentState.copy(commandList = _commandList.toList())
        }
        updateNewName()
    }

    fun updateNewName()
    {
        if (folderFileList != null && folderFileList.isEmpty()) return

        var index = 0
        for (file in filterFileList){
            if (!file.enabled) {
                file.newName = ""
                continue
            }
            file.newName = generateFilename(file.oldName,_commandList,index)
            index++

        }

        _uiState.updateAndGet { currentState ->
            currentState.copy(fileList = filterFileList)
        }
        updateCanRename()
        fakeUpdate()
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
                TextCommand.enumerate -> enumerate(name,index+c.value1.toInt(),c.value2)
                TextCommand.lowercase -> lowercase(name)
                TextCommand.uppercase -> uppercase(name)
                TextCommand.removeAmount -> removeAmount(name,c.value1.toIntOrNull(),c.value2)
                TextCommand.capitalize -> capitalize(name,c.value1)
                TextCommand.removeUntil -> removeUntil(name,c.value1,c.value2)
            }
        return name
    }
}