extern crate iron;
extern crate logger;
extern crate mustache;
#[macro_use]
extern crate router;
extern crate rustc_serialize;

use std::env;
use std::io::Cursor;
use iron::prelude::{
    Chain, Iron, IronResult, Request, Response
};
use iron::mime::Mime;
use logger::Logger;
use router::Router;

#[derive(RustcEncodable)]
struct Timeline {
    user: String
}

fn timeline(request: &mut Request) -> IronResult<Response> {
    let html: Mime = "text/html; charset=UTF-8".parse().unwrap();

    let router = request.extensions.get::<Router>().unwrap();
    let user = router.find("user").unwrap();
    let template = mustache::compile_path("templates/timeline.html").unwrap();

    let timeline = Timeline {
        user: String::from(user)
    };

    let mut rendered_timeline = Cursor::new(Vec::<u8>::new());
    template.render(&mut rendered_timeline, &timeline).unwrap();
    Ok(Response::with((
        iron::status::Ok,
        html,
        rendered_timeline.into_inner()
    )))
}

fn main() {
    let port = env_port(5000);

    let router = router!(
        get "/:user" => timeline
    );
    let mut chain = Chain::new(router);
    chain.link(Logger::new(None));

    Iron::new(chain)
        .http(("0.0.0.0", port))
        .unwrap();
}

// Surely this should have a better error type.
fn env_port(default: u16) -> u16 {
    let port_var = env::var("PORT").ok();
    let port_num = match port_var {
        Some(s) => s.parse::<u16>().ok(),
        None    => None,
    };

    port_num
        .unwrap_or(default)
}

#[cfg(test)]
mod env_port {
    use super::env_port;
    use std::env;

    #[test]
    fn returns_environment_port() {
        env::set_var("PORT", "1234");
        assert_eq!(1234, env_port(5000));
    }

    #[test]
    fn returns_default_when_unset() {
        env::remove_var("PORT");
        assert_eq!(5000, env_port(5000));
    }

    #[test]
    fn returns_default_when_not_integral() {
        env::set_var("PORT", "BANANA");
        assert_eq!(5000, env_port(5000));
    }
}
