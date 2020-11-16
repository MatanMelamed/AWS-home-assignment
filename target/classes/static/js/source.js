
function getFile() {

    document.getElementById("upfile").click();
}

function sub(obj) {
    var file = obj.value;
    var fileName = file.split("\\");
    document.getElementById("selectFileButton").innerHTML = fileName[fileName.length - 1];
    // document.getElementById("selectFileButton").style.color = "blue"
    // document.myForm.submit();
    // event.preventDefault();
}

function on_upload(){
    alert("The form was submitted");
}
