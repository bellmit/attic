({
//	appDir: '${project.basedir}/src/main/webapp',
	baseUrl: '${project.basedir}/src/main/js',
	dir: '${project.build.directory}/requirejs',
	mainConfigFile: '${project.basedir}/src/main/js/site.js',
	wrap: true,
	optimize: 'none',
	modules: [
		{
			name: 'site'
		}
	]
})
