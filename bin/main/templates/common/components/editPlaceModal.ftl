<!--Edit Modal -->
<#if place??>
    <div class="modal fade" id="editPlaceModal${place.id}" tabindex="-1" aria-labelledby="editModalLabel"
     aria-hidden="true" data-bs-backdrop="static">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="editModalLabel">Edit Place</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"
                        aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form method="post" action="/places">
                    <!-- ID row -->
                    <div class="col-sm-10" id="placeId">
                        <label for="placeId" class=" col-form-label">ID
                            : ${place.id}</label>
                        <input type="hidden" name="placeId"
                               value="${place.id}">
                    </div>
                    <!-- street row -->

                    <label for="street" class="col-sm-2 col-form-label">Street</label>
                    <div class="col-sm-6">
                        <input type="text" class="form-control" name="street"
                               value="${place.address.street}">
                    </div>
                    <!-- cross street row -->

                    <label for="crossStreet" class="col-form-label">Cross-Street</label>
                    <div class="col-sm-6">
                        <input type="text" name="cross_street" class="form-control"
                               value="${place.address.crossStreet}">
                    </div>
                    <!-- Position row -->
                    <label for="position" class="col-form-label mt-2">Position</label>
                    <div class="col-sm-6">
                        <select id=positionList onchange="update()" class="form-select" name="position"
                                aria-label="Select position" required>

                            <option name="choice" id="choice" value="${place.address.streetPosition}" selected>${place.address.streetPosition}</option>
                            <option value="Nord_east">Nord East</option>
                            <option value="nord_west">Nord West</option>
                            <option value="south_east">South East</option>
                            <option value="south_west">South West</option>
                            <option value="n_a">N/A</option>

                        </select>

                    </div>
                    <!--lat / long -->
                    <div class="row">
                        <div class="col">
                            <label for="latitude" class="col-form-label">Latitude</label>
                            <div class="col-sm-6">
                                <input type="text" inputmode="decimal" name="latitude" class="form-control"
                                       value="${place.coordinates.latitude?c}">
                            </div>
                        </div>
                        <#if date?? && code??>
                            <input type="hidden" name="date" value="${date?c}">
                        <input type="hidden" name="code" value="${code}">
                        </#if>


                        <div class="col mb-3">
                            <label for="longitude" class="col-form-label">Longitude</label>
                            <div class="col-sm-6">
                                <input type="text" inputmode="decimal" name="longitude" inputmode="numeric"
                                       class="form-control"
                                       value="${place.coordinates.longitude?c}">
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                            Close
                        </button>
                        <button type="submit" class="btn btn-primary">Save changes</button>
                    </div>
                </form>


            </div>

        </div>
    </div>
</div>

</#if>
