async function hashearContrasenia() {
    const passwordInput = document.getElementById('password');
    const plainPassword = passwordInput.value;

    // Si la contraseña está vacía, no continuamos
    if (!plainPassword) {
        alert("Por favor, ingrese una contraseña.");
        return false; // Prevenir el envío del formulario
    }

    // Generar un salt y hashear la contraseña
    try {
        const bcrypt = require('bcrypt');
        const saltRounds = 10;
        const salt = await bcrypt.genSalt(10);
        const hashedPassword = await bcrypt.hash(plainPassword, salt);

        // Reemplazar el valor del campo de contraseña con el hash
        passwordInput.value = hashedPassword;

        // Continuar con el envío del formulario
        return true;
    } catch (error) {
        console.error("Error al hashear la contraseña:", error);
        alert("Ocurrió un error al procesar la contraseña.", error);

        return false; // Prevenir el envío del formulario
    }
}