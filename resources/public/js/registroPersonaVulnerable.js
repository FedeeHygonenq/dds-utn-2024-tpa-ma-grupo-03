let minorCount = 0;
const tiposDocumento = JSON.parse(document.getElementById('document-types-data').textContent);
document.getElementById('addMinorBtn').addEventListener('click', function () {
    const minorList = document.getElementById('minorList');
    minorCount += 1;
    const modalId = `registroModal${minorCount}`;

    const newMinor = document.createElement('div');
    newMinor.className = 'list-group-item d-flex justify-content-between align-items-center';
    newMinor.innerHTML = `
        <span>Menor ${minorCount} -</span>
        <span>
            <a class="text-primary" data-bs-toggle="modal" data-bs-target="#${modalId}">REGISTRAR</a>
            <a class="text-danger ms-2 remove-minor">X</a>
        </span>
    `;

    // Add the new element to the list
    minorList.appendChild(newMinor);

    // Add the remove event to the link with the class 'remove-minor'
    newMinor.querySelector('.remove-minor').addEventListener('click', function (event) {
        event.preventDefault();
        newMinor.remove();
        document.getElementById(modalId).remove();
    });

    // Create the modal for the new minor
    const modalChildList = document.getElementById('modalChildList');
    const newModal = document.createElement('div');
    newModal.className = 'modal fade';
    newModal.id = modalId;
    newModal.tabIndex = -1;
    newModal.setAttribute('aria-labelledby', 'registroModalLabel');
    newModal.setAttribute('aria-hidden', 'true');

    let tipoDocumentoOptions = `<option selected>Seleccioná una opción</option>`;
    tiposDocumento.forEach(function(tipo) {
        tipoDocumentoOptions += `<option value="${tipo}">${tipo}</option>`;
    });

    newModal.innerHTML = `
        <div class="modal-dialog modal-dialog-centered modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h1 class="modal-title text-center" id="registroModalLabel"><strong>Registro de menor a cargo</strong></h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="container d-flex justify-content-center align-items-center">
                        <div class="col-md-8 contribucionChildForm">
                            <div class="row">
                                <div class="col mb-2">
                                    <label for="firstNameFormChild${minorCount}" class="form-label" >Nombre(s)*</label>
                                    <input type="text" class="form-control" id="firstNameFormChild${minorCount}" name="nombreHijo" placeholder="Juan" required>
                                </div>
                                <div class="col mb-2">
                                    <label for="lastnameFormChild${minorCount}" class="form-label">Apellido(s)*</label>
                                    <input type="text" class="form-control" id="lastnameFormChild${minorCount}" name="apellidoHijo" placeholder="Perez" required>
                                </div>
                            </div>
                            <div class="mb-2">
                                <label for="birthdayFormChild${minorCount}" class="form-label">Fecha de nacimiento*</label>
                                <input type="date" class="form-control" id="birthdayFormChild${minorCount}" name="fechaNacimientoHijo" required>
                            </div>
                            <div class="mb-2">
                                <div class="row">
                                    <div class="col mb-2">
                                        <label class="form-label">Tipo documento*</label>
                                        <select class="form-select" name="tipoDocumentoHijo" required>
                                            ${tipoDocumentoOptions}
                                        </select>
                                    </div>
                                    <div class="col mb-2 fs-6">
                                        <label for="nroDocumentoChild${minorCount}" class="form-label">Nro documento*</label>
                                        <input type="number" class="form-control" id="nroDocumentoChild${minorCount}" name="nroDocumentoHijo" placeholder="20123456" required>
                                    </div>
                                </div>
                            </div>
                            <strong class="text-center"> Se debene llenar todos los campos</strong>
                            <div class="d-flex justify-content-center align-items-center m-5">
                                <button class="btn btn-lng boton-registrar" type="button" data-bs-dismiss="modal">Listo</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `;

    modalChildList.appendChild(newModal);
});
/*
document.getElementById('contribucionForm').addEventListener('submit', function (event) {
    const minorListInput = document.getElementById('minorListInput');
    const minors = [];

    // Collect the list of minors
    const childForms = document.querySelectorAll('.contribucionChildForm');
    childForms.forEach(form => {
        const formData = new FormData(form);
        const minorData = {};
        formData.forEach((value, key) => {
            minorData[key] = value;
        });
        minors.push(minorData);
    });

    // Update the hidden input field with the list of minors
    minorListInput.value = JSON.stringify(minors);
});*/