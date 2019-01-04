# Heroku Build Config Buildpack

Takes a `.heroku-build.yml` YAML file and uses it to provide env vars that are only visible at build time. Environment variables defined this way will not pollute the runtime environment of the application, and can be tracked in source control.

Usage:

```bash
heroku apps:create
heroku buildpacks:add https://github.com/ojacobson/heroku-buildpack-build-config.git
heroku buildpacks:add heroku/nodejs

cat > .heroku-build.yml <<'BUILD'
env:
    NPM_CONFIG_PRODUCTION: false
BUILD

git add .heroku-build.yml
git commit -m 'Install devDependencies when building Node app.'
git push heroku master
```
