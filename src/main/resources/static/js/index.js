function handleUploadPhoto(){
    $("#uploadLoader").css("display","block")
    $(".uploadNotificationDiv").css("display","none")


    var formData = new FormData();
    formData.append('file', $('#inputFile')[0].files[0]);

    $.ajax({
             type : "POST",
             url : '/upload',
             data: formData,
             contentType: false,
             processData: false,
             crossDomain : true,
             success : function(data) {
                 onUploadPhotoSuccess();
             },
             error : function(data) {
                onUploadPhotoFail()
             }
         });
 }

function onUploadPhotoSuccess() {
    $("#uploadLoader").css("display","none")
    $(".uploadNotificationDiv").html("Successfully uploaded the photo.")
    $(".uploadNotificationDiv").css("display","block")
}

function onUploadPhotoFail() {
    $("#uploadLoader").css("display","none")
    $(".uploadNotificationDiv").html("Upload photo failed.")
    $(".uploadNotificationDiv").css("display","block")
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
    $(".searchNotificationDiv").css("display","block")
}

