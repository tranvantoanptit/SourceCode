<link rel="stylesheet" type="text/css" href="./css/header_footer.css" />
</head>
<body>
<ul class="topnav" id="myTopnav">
  <!-- <li><a class="active" href="#home">Home</a></li> -->
  <li><a href="#help">Help</a></li>
  <li><a href="inquiry2.jsp">Inquiry</a></li>
  <li><a href="upload.jsp">Upload</a></li>
  <li><a href="index.html">Home</a></li>
  <li class="icon">
    <a href="javascript:void(0);" style="font-size:15px;" onclick="myFunction()">☰</a>
  </li>
</ul>

<script>
function myFunction() {
    var x = document.getElementById("myTopnav");
    if (x.className === "topnav") {
        x.className += " responsive";
    } else {
        x.className = "topnav";
    }
}
</script>