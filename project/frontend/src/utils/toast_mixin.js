import Swal from 'sweetalert2';

const Toast = Swal.mixin({
  toast: true,
  position: 'top-end',
  showConfirmButton: false,
  timer: 1500,
  timerProgressBar: true,
  onOpen: (toast) => {
    toast.addEventListener('mouseenter', Swal.stopTimer)
    toast.addEventListener('mouseleave', Swal.resumeTimer)
  }
})
const swalWithBootstrapButtons = Swal.mixin({
  customClass: {
    confirmButton: 'btn btn-primary mr-3',
    cancelButton: 'btn btn-secondary',
    icon: 'swal-icon-class'
  },
  buttonsStyling: false
})
export function toastSuccess(message="Success",timer=1500) {
  let html_all = "<span class='toast-font'> <i class='fa fa-check-circle-o m3 fa-large' aria-hidden='true'></i> "+message+"</span>"
  console.log("reach util component")
  Toast.fire({
    html: html_all,
    timer:timer,
    background: '#27ae60',
  })
}
export function toastFailure(message="Action Failed", timer=2000) {
  let html_all = "<span class='toast-font'> <i class='fa fa-exclamation-circle m3 fa-large' aria-hidden='true'></i> "+message+"</span>"
  console.log("reach util component")
  Toast.fire({
    html: html_all,
    timer:timer,
    background: '#e74c3c'
  })
}

export function swal_confirm(message="You won't be able to revert this!", buttonName="Delete") {
  return new Promise(function(resolve, reject) {
    swalWithBootstrapButtons.fire({
      icon: 'question',
      iconHtml: "<i class='fa fa-exclamation-circle'></i>",
      title:'Are you sure?',
      text: message,
      showCancelButton: true,
      confirmButtonText: buttonName
    }).then((result) => {
      if (result.value) {
        resolve()
      }
      else {
        reject()
      }
    })
  })
}

