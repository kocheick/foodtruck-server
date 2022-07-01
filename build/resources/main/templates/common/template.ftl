<#macro mainLayout title="Welcome Halal Finder" activePage="active">
<!doctype html>
<html lang="en">
<head>
    <title>${title}</title>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <link href="static-resources/styles.css">
</head>
<body>
<#include "navbar.ftl">
<div class="container">
    <div class="row px-3 m-1">
        <#if user??>
        <h3>Hello, ${user.username}</h3>
        </#if>
    </div>
    <div class="row m-1">
            <#nested/>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p" crossorigin="anonymous"></script>
<#--<script src="static-resources/script.js"></script>-->
</body>
</html>
</#macro>