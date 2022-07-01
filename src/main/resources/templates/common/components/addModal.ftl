<!--Edit Modal -->

<div class="modal fade" id="addModal" tabindex="-1" aria-labelledby="editModalLabel"
     aria-hidden="true" data-bs-backdrop="static" >
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addModalLabel">Add Place</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"
                        aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form method="post" action="/places">
                    <!-- street row -->
                    <label for="street" class="col-sm-2 col-form-label">Street</label>
                    <div class="col-sm-6">
                        <input type="text" class="form-control" id="street" name="street">
                    </div>
                    <!-- cross street row -->

                    <label for="crossStreet" class="col-form-label">Cross-street</label>
                    <div class="col-sm-6">
                        <input type="text" name="cross_street" class="form-control">
                    </div>
                    <!-- Position row -->
                    <label for="position" class="col-form-label mt-2">Position</label>
                    <div class="col-sm-6">
                        <select class="form-select" name="position"
                                aria-label="Select position">
                            <option value="N_A" selected disabled>Select position</option>
                            <option value="Nord_east">Nord East</option>
                            <option value="nord_west">Nord West</option>
                            <option value="south_east">South East</option>
                            <option value="south_west">South West</option>

                        </select>
                    </div>
                    <!--lat / long -->
                    <div class="row">
                        <div class="col">
                            <label for="latitude" class="col-form-label">Latitude</label>
                            <div class="col-sm-6">
                                <input type="text" inputmode="numeric" name="latitude" class="form-control">
                            </div>
                        </div>
                        <#if date?? && code??>
                            <input type="hidden" name="date" value="${date?c}">
                            <input type="hidden" name="code" value="${code}">
                        </#if>

                        <div class="col mb-3">
                            <label for="longitude" class="col-form-label">Longitude</label>
                            <div class="col-sm-6">
                                <input type="text" inputmode="numeric"  name="longitude"
                                       class="form-control">
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                            Cancel
                        </button>
                        <button type="submit" class="btn btn-primary">Add New Place</button>
                    </div>
                </form>


            </div>

        </div>
    </div>
</div>
