#[macro_use]
extern crate glium;

use glium::DisplayBuild;

#[derive(Copy, Clone)]
struct Vertex {
    position: [f32; 2],
}

implement_vertex!(Vertex, position);

fn display() -> Result<glium::Display, glium::GliumCreationError<glium::glutin::CreationError>> {
    glium::glutin::WindowBuilder::new()
        .with_dimensions(640, 480)
        .with_vsync()
        .build_glium()
}

fn program(display: &glium::Display) -> Result<glium::Program, glium::ProgramCreationError> {
    let vertex_shader_src = r#"
        #version 330

        in vec2 position;

        void main() {
            gl_Position = vec4(position, 0.0, 1.0);
        }
    "#;

    let fragment_shader_src = r#"
        #version 330

        out vec4 color;

        void main() {
            color = vec4(1.0, 0.0, 0.0, 1.0);
        }
    "#;

    glium::Program::from_source(
        display,
        vertex_shader_src,
        fragment_shader_src,
        None
    )
}

fn tri() -> Vec<Vertex> {
    let v1 = Vertex { position: [-1.0, -1.0] };
    let v2 = Vertex { position: [ 1.0, -1.0] };
    let v3 = Vertex { position: [ 0.0,  1.0] };
    vec![v1, v2, v3]
}

fn main() {
    use glium::Surface;

    let display = display().unwrap();

    let shape = tri();
    let vertex_buffer = glium::VertexBuffer::new(&display, &shape).unwrap();
    let indices = glium::index::NoIndices(glium::index::PrimitiveType::TrianglesList);
    let program = program(&display).unwrap();
    let uniforms = uniform! {};
    let draw_params = Default::default();

    loop {

        let mut target = display.draw();
        target.clear_color(0.0, 0.0, 1.0, 1.0);
        target.draw(
            &vertex_buffer,
            &indices,
            &program,
            &uniforms,
            &draw_params,
        ).unwrap();
        target.finish().unwrap();

        for ev in display.poll_events() {
            match ev {
                glium::glutin::Event::ReceivedCharacter(_) => return,
                glium::glutin::Event::Closed => return,
                _ => (),
            }
        }
    }
}
