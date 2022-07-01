<nav class="navbar navbar-expand-sm navbar-light bg-light sticky-top">
    <div class="container-fluid">
        <a class="navbar-brand ms-2" href="/">Truck Finder</a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarResponsive"
                aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse " id="navbarResponsive">
            <ul class="navbar-nav mt-2 ms-auto text-end me-2 ">
                <li class="nav-item">
                    <a class="nav-link " href="/">Home</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link active " href="/places/management">Trucks</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/users/management">Users</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="/about">About</a>
                </li>
                <div>

                </div>
                <#if user??>
                    <li><p class="nav-link ms-4 ">Welcome ${user.username}</p></li>
                    <li><a class="nav-link" role="button" href="/users/logout">Sign Out</a></li>
                <#else>
                    <li class="nav-item"><a class="nav-link" href="/users/login">Sign In</a></li>
                    <li class="nav-item"><a class="nav-link" href="/users/signup">Sign Up</a></li>
                </#if>
            </ul>
        </div>
    </div>
</nav>