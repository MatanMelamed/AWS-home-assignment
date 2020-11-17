$(document).ready(function () {

console.log(' on document ready')


})


function handleGetPhotos(){
    console.log("handleGetPhotos");

    const key = $("input[name*='searchKey']").val();

     $.ajax({
                type : "GET",
                url : '/showPhotos',
                data: `search_key=${key}`,
                crossDomain : true,
                success : function(data) {
                    processResponse(data);
                },
                error : function(data) {

                }
            });
        }





 function processResponse(data) {
     console.log("Your response processing goes here..", data);
     data.forEach(item => {
     $("#photos").append(`<img src=\"${item}\" />`)
     })
 }

