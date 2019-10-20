function requestLanguage() {
    const file = document.getElementById("customFile").files[0];
    if (file == null) {
        return;
    }
    const formData = new FormData();
    formData.append("file", file);
    const time1 = new Date().getTime();
    loadData(formData, "speech/shortWord", {
        execute: function (response1) {
            const time2 = new Date().getTime();
            loadData(formData, "speech/frequentWord", {
                execute: function (response2) {
                    const time3 = new Date().getTime();
                    renderResponse({
                        shortWord: JSON.parse(response1),
                        frequentWord: JSON.parse(response2),
                        shortWordTime: time2 - time1,
                        frequentWordTime: time3 - time2,
                        file: file
                    })
                },
                error: function (xhr) {
                    errorDuringFetchingData(xhr);
                }
            });
        },
        error: function (xhr) {
            errorDuringFetchingData(xhr);
        }
    });
}

function loadData(formData, uri, callback) {
    const request = new XMLHttpRequest();
    request.open("POST", uri);
    request.send(formData);
    request.onload = function (e) {
        if (request.status === 200) {
            callback.execute(request.responseText)
        } else {
            callback.error(request)
        }
    };
}

function errorDuringFetchingData(xhr) {
    console.error(xhr)
}

function renderResponse(result) {
    const rowContainer = document.getElementById("row_container");
    rowContainer.innerHTML = "";
    const shortWordLanguage = result.shortWord.languages.FRENCH > result.shortWord.languages.GERMAN ? "Французкий" : "Немецкий";
    const frequentWordLanguage = result.frequentWord.languages.FRENCH > result.frequentWord.languages.GERMAN ? "Французкий" : "Немецкий";
    rowContainer.innerHTML = "" +
        "<div class=\"col-sm-4\">" +
        "   <h3>Метод коротких слов.</h3>" +
        "   <p>Вероятность немецкого языка - " + (result.shortWord.languages.GERMAN) + "</p>" +
        "   <p>Вероятность французского языка - " + (result.shortWord.languages.FRENCH) + "</p>" +
        "   <p>Язык документа - " + shortWordLanguage + "</p>" +
        "   <p>Время выполнения - " + result.shortWordTime + " миллисекунд.</p>" +
        "</div>" +
        "<div class=\"col-sm-4\">" +
        "   <h3>Метод частотных слов.</h3>" +
        "   <p>Вероятность немецкого языка - " + (result.frequentWord.languages.GERMAN) + "</p>" +
        "   <p>Вероятность французского языка - " + (result.frequentWord.languages.FRENCH) + "</p>" +
        "   <p>Язык документа - " + frequentWordLanguage + "</p>" +
        "   <p>Время выполнения - " + result.frequentWordTime + " миллисекунд.</p>" +
        "</div>" +
        "<div class=\"col-sm-4\">" +
        "   <h3>Исходный файл.</h3>" +
        "   <a download href='" + URL.createObjectURL(result.file) + "' class=\"badge badge-light\">Документ</a>" +
        "</div>";
}