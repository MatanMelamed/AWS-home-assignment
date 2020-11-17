function handleUploadPhoto(){
    $("#uploadLoader").css("display","block")
    $(".uploadNotificationDiv").css("opacity","0")


    var formData = new FormData();
    formData.append('file', $('#inputFile')[0].files[0]);

    $.ajax({
             type : "POST",
             url : '/upload',
             data: formData,
             contentType: false, // NEEDED, DON'T OMIT THIS (requires jQuery 1.6+)
             processData: false, // NEEDED, DON'T OMIT THIS
             crossDomain : true,
             success : function(data) {
                 onUploadPhotoSuccess();
             },
             error : function(data) {
                onUploadPhotoFail()
             }
         });
 }

function onUploadPhotoSuccess(data) {
    $("#uploadLoader").css("display","none")
    $(".uploadNotificationDiv").html("Successfully uploaded the photo.")
    $(".uploadNotificationDiv").css("opacity","100")
}

function onUploadPhotoFail(data) {
    $("#uploadLoader").css("display","none")
    $(".uploadNotificationDiv").html("Upload photo failed.")
    $(".uploadNotificationDiv").css("opacity","100")
}


function handleSearchPhotos(){
    $("#photos").empty();
    $("#searchLoader").css("display","block")
    $(".searchNotificationDiv").css("display","none")

    const key = $("#searchKey").val();

     $.ajax({
                type : "GET",
                url : '/showPhotos',
                data: `search_key=${key}`,
                crossDomain : true,
                success : function(data) {
                    $("#searchLoader").css("display","none")
                    processSearchPhotosResponse(data);
                },
                error : function(error) {
                    onSearchPhotosFail();
                }
            });
}

function processSearchPhotosResponse(data) {
     data.forEach(item => {
     $("#photos").append(`<img src=\"${item}\" />`)
     })
}

function onSearchPhotosFail() {
    $("#searchLoader").css("display","none")
    $(".searchNotificationDiv").html("Search photos failed.")
    $(".searchNotificationDiv").css("opacity","100")
}

