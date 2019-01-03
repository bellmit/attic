extern crate libc;

use std::ffi::{CString};
use std::ptr;

fn mount() {
    let src     = CString::new("install-amd64-minimal-20170202.iso").unwrap().as_ptr();
    let target  = CString::new("/mnt").unwrap().as_ptr();
    // let fstype  = CString::new("iso9660").unwrap().as_ptr();
    let options = ptr::null_mut(); //CString::new("loop").unwrap().as_ptr();
    unsafe {
        libc::mount(src, target, /* fstype, */ 0, options);
    }
}

fn main() {
    println!("hello");
    mount();
}
