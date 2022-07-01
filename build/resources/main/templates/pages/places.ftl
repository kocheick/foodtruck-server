<#import "../common/template.ftl" as layout>
<@layout.mainLayout title="Places Management">
    <#include "../common/components/search.ftl">
    <#if places?? && (places?size>0)>

        <div class="table-responsive">
        <table class="table table-hover">
            <thead class="thead-dark">
            <tr>
                <th scope="col">Street</th>
                <th scope="col">Cross-street</th>
                <th scope="col">Position</th>
                <th scope="col">Latitude</th>
                <th scope="col">Longitude</th>
                <th scope="col">Votes</th>
            </tr>
            </thead>

            <tbody>

            <#list places as place>
                <tr>
                    <td>${place.address.street}</td>
                    <td>${place.address.crossStreet}</td>
                    <td>${place.address.streetPosition}</td>
                    <td>${place.coordinates.latitude?c}</td>
                    <td>${place.coordinates.longitude?c}</td>
                    <td>${place.numberOfVotes}</td>
                    <input type="hidden" name="date" value="${date?c}">
                    <input type="hidden" name="code" value="${code}">
                    <td></td>
                    <td>
                        <a href="/places?action=edit&id=${place.id}" data-bs-toggle="modal"
                           data-bs-target="#editPlaceModal${place.id}"
                           class="btn btn-sm btn-secondary ms-auto me-2 float-start"
                           role="button" id="editButton">Edit</a>

                        <a href="/delete?id=${place.id}" data-bs-toggle="modal"
                           data-bs-target="#removePlaceModal${place.id}"
                           class="btn btn-sm btn-danger float-start me-2 mt-1  mt-lg-0"
                           role="button">Delete</a>
                    </td>

                </tr>
                <#include "../common/components/editPlaceModal.ftl">
                <#include "../common/components/removePlaceModal.ftl">

            </#list>

            </tbody>

        </table>
        <div class="row col-4 position-fixed bottom-0 mb-2 shadow-lg p-1 rounded">
            <a href="/places?action=new" data-bs-toggle="modal"
               data-bs-target="#addModal" class="btn btn-success float-right" role="button">Add Place</a>
        </div>
    <#else>

        <div class=" row col-5 mx-auto align-middle mt-5 p-1 rounded">
            <a href="/places?action=new" data-bs-toggle="modal"
               data-bs-target="#addModal" class="btn btn-success float-right mt-5" role="button">Add Place</a>
        </div>
    </#if>
    <#include "../common/components/addModal.ftl">

    </div>


</@layout.mainLayout>