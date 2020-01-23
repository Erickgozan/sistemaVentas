$(document).ready(function () {

    $("td #btnDelete").click(function (e) {
        e.preventDefault();
        var idp = $(this).parent().find("#idp").val();
        Swal.fire({
            title: '¿Estas seguro de eliminar?',
            text: "¡No podrás revertir esto!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Si, eliminar!'
        }).then((result) => {
            if (result.value) {
                eliminar(idp);
                Swal.fire(
                    'Eliminado!',
                    'El producto se ha eliminado de tu carrito',
                    'success'
                ).then((result) => {
                    if (result.value) {
                        parent.location.href = "Controlador?accion=carrito";
                    }
                });
            } else {
                Swal.fire('El producto no se pudo eliminar');
            }
        });

    });

    function eliminar(idp) {
        var url = "Controlador?accion=delete";
        $.ajax({
            type: "POST",
            url: url,
            data: "idp=" + idp,
            success: function (data) {

            }
        });
    }
    
    $("td #cantidad").click(function (){
        var idp = $(this).parent().find("#idpro").val();
        var cantidad = $(this).parent().find("#cantidad").val();
        var url = "Controlador?accion=actualizarCantidad";
        
        $.ajax({
            type:'POST',
            url: url,
            data: "idp="+idp+"&cantidad="+cantidad,
            success:function(data){
                location.href="Controlador?accion=carrito";
            }
        });
    });
});