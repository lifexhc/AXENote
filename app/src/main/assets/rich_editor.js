// JavaScript Document

window.onload = initEditor;

<!-- 通知Android编辑器 -->
function initEditor() {
    window.EditorActivity.initEditor();
}

<!-- 设置主题 -->
function initTheme(isNightMode) {
    var editor = document.getElementById("editor");
    if (isNightMode == 1) {
        document.body.style.backgroundColor = "#4f4f4f";
        editor.style.backgroundColor = "#4f4f4f";
    } else {
        document.body.style.backgroundColor = "#fefcef";
        editor.style.backgroundColor = "#fefcef";
    }
}

<!-- 初始化便签内容 -->
function initContent(html) {
    var editor = document.getElementById("editor");
    editor.innerHTML = html;
}

<!-- 插入图片 -->
function insertImg(base64) {
    var html = '<img src="data:image/jpeg;base64,' + base64 + '"/>';
    html += '<br/>';
    document.execCommand('insertHTML', false, html);
}

<!-- 获取文本内容 -->
function getEditorContent() {
    var editor = document.getElementById("editor");
    var imgCount = editor.getElementsByTagName("img").length;
    var html = editor.innerHTML;
    var text = editor.textContent;
    var title;
    if (text.length > 16) {
        title = text.substring(0, 16);
    } else {
        title = text;
    }
    window.AndroidEditor.getEditorContent(title, html, imgCount);
}
