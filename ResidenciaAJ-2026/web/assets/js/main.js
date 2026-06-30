document.addEventListener("DOMContentLoaded", function() {
    // Función para cargar componentes HTML de forma dinámica (Modularidad)
    function loadComponent(elementId, filePath) {
        fetch(filePath)
            .then(response => {
                if (!response.ok) {
                    throw new Error('No se pudo cargar ' + filePath);
                }
                return response.text();
            })
            .then(data => {
                document.getElementById(elementId).innerHTML = data;
            })
            .catch(error => console.error('Error al cargar componente:', error));
    }

    // Cargar el header y el footer si los contenedores existen en la página
    if (document.getElementById('header-placeholder')) {
        loadComponent('header-placeholder', 'components/header.html');
    }
    
    if (document.getElementById('footer-placeholder')) {
        loadComponent('footer-placeholder', 'components/footer.html');
    }
});
