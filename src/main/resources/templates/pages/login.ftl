<#-- @ftlvariable name="error" type="java.lang.String" -->
<#import "../common/template.ftl" as base/>

<@base.mainLayout>
  <div class="mx-auto mt-4 col-7 col-lg-4 align-items-center">
    <h3 class=" my-4 text-center">Log In</h3>
    <div class="container">
      <form action="/users/login" method="POST">
        <#if error??>
          <p>${error}</p>
        </#if>

        <div class="form-group mt-1">
          <label for="email">Emaill address</label>
          <input type="email" name="email" class="form-control" id="email" placeholder="Email">
        </div>
        <div class="form-group mt-1">
          <label for="password">Password</label>
          <input type="password" name="password" class="form-control" id="password"
                 placeholder="Password">
        </div>
        <div class="text-center">
          <button type="submit" class="btn btn-success my-5">Log In</button>

        </div>
      </form>
    </div>
  </div>
</@base.mainLayout>

