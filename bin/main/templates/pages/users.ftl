<#import "../common/template.ftl" as layout />
<@layout.mainLayout title="Users Management">
    <div class="table-responsive">
        <table class="table">
            <thead class="thead-dark">
            <tr>
                <th scope="col">Username</th>
                <th scope="col">Email</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <#if users??>
                <#list users as user>
                    <tr>
                        <td>${user.username}</td>
                        <td>${user.email}</td>
                        <td>
                            <a href="/employee?action=edit&id=${user.id}" class="btn btn-sm btn-secondary ms-auto me-2 float-start"
                               role="button">Edit</a>

                            <a href="/delete?id=${user.id}" class="btn btn-sm btn-danger float-start me-2 mt-1 mt-md-0"
                               role="button">Delete</a>
                        </td>
                    </tr>
                </#list>
            </#if>
            </tbody>
        </table>
    </div>
    <div class="container">
        <div class="row">
            <a href="/users?action=new" class="btn btn-secondary float-right" role="button">New User</a>
        </div>
    </div>
</@layout.mainLayout>