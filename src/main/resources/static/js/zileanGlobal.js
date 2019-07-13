$("#delayed").click(function () {
    var indexBodyDoc = $('#indexBody');
    var data = "./html/body/delayedList.html";
    var attr = indexBodyDoc.attr("data");
    if (null != attr && data === attr) {
        return;
    }

    clearInterval(indexT);
    indexBodyDoc.html();
    indexBodyDoc.load(data);
    indexBodyDoc.attr("data", data);

    // todo indexBody 切换主体内容，请清除定时器
});
