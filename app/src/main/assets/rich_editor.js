// JavaScript Document

window.onload = initEditor;

<!-- 插入图片 -->
function insertImg(base64) {
    var html = '<img src="data:image/jpeg;base64,' + base64 + '"/>';
    document.execCommand('insertHTML', false, html);
}

<!-- 加载完毕调用initContent() -->
function initEditor() {
    window.EditorActivity.initEditor();
}

<!-- 初始化便签内容 -->
function initContent(html) {
    var editor = document.getElementById("editor");
    editor.innerHTML = html;
}

<!-- 获取文本内容 -->
function getEditorContent() {
    var editor = document.getElementById("editor");
    window.AndroidEditor.getEditorContent(editor.innerHTML);
}