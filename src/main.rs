#[macro_use]
extern crate nickel;

use nickel::Nickel;
use nickel::router::router::Router;
use std::env;

fn main() {
    let port = env_port(5000);

    let mut server = Nickel::new();

    server.utilize(routes());

    server.listen(("0.0.0.0", port));
}

fn routes() -> Router {
    router! {
        get "**" => |_req, _res| {
            "Hello world!"
        }
    }
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
