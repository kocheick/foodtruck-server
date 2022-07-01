<!--Remove Modal -->
<#if place??>
    <div class="modal fade" id="removePlaceModal${place.id}" tabindex="-1" aria-labelledby="editModalLabel"
     aria-hidden="true">
    <div class="modal-dialog modal-md modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="editModalLabel">Remove Place?</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"
                        aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form method="post" action="/places/${place.id}">
                    <div class="ms-2">
                        <label for="id" class="row col-10 col-form-label">Place ID<p class="fw-bold">${place.id} </p>
                        </label>
                        <input type="hidden" name="placeId"
                               value="${place.id}">
                        <label for="street" class="row col-8 col-form-label">Street<p
                                    class="fw-bold">${place.address.street} </p></label>
                        <label for="crossstreet" class="row col-10 col-form-label">Cross-street<p
                                    class="fw-bold"> ${place.address.crossStreet}</p></label>
                        <label for="lat" class="row col-6 col-form-label">Latitude<p
                                    class="fw-bold">${place.coordinates.latitude} </p></label>
                        <label for="long" class="row col-6 col-form-label">Longitude<p
                                    class="fw-bold"> ${place.coordinates.longitude}</p></label>
                        <#if code?? && date??>
                            <input type="hidden" name="date" value="${date?c}">
                            <input type="hidden" name="code" value="${code}">
                        </#if>

                    </div>


                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                            Cancel
                        </button>
                        <button type="submit" class="btn btn-danger" id="submit">Confirm</button>
                    </div>
                </form>


            </div>

        </div>
    </div>
</div>

</#if>
