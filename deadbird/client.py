import click
import tweepy

from . import usersettings

def make_settings():
    settings = usersettings.Settings('ca.grimoire.deadbird.Client')
    settings.add_setting('consumer_key', str, default='3wydsN4GVUFxWRW1FAIXQZ0qd')
    settings.add_setting('consumer_secret', str, default='74luU4gcGH3eZ4aANVS0nbGnmnyF1EkCq8alq2UEebBECszkUA')
    settings.add_setting('access_token', str, default=None)
    settings.add_setting('access_token_secret', str, default=None)
    settings.load_settings()
    return settings

def login(settings, auth):
    redirect_url = auth.get_authorization_url()

    click.launch(redirect_url)
    verifier = click.prompt('Verifier')

    auth.get_access_token(verifier)
    settings.access_token = auth.access_token
    settings.access_token_secret = auth.access_token_secret
    return tweepy.API(auth)

def try_resume(settings, auth):
    if settings.access_token is None or settings.access_token_secret is None:
        return None
    auth.set_access_token(settings.access_token, settings.access_token_secret)
    api = tweepy.API(auth)
    try:
        api.verify_credentials()
        return api
    except tweepy.TweepError:
        return None

def make_api():
    settings = make_settings()

    auth = tweepy.OAuthHandler(settings.consumer_key, settings.consumer_secret)
    api = try_resume(settings, auth)
    if api is None:
        api = login(settings, auth)

    settings.save_settings()
    return tweepy.API(auth)
