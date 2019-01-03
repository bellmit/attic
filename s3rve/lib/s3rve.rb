require './lib/s3rve/site'
require './lib/s3rve/bucket'

module S3rve
  def self.publish!(site_config)
    site = Site.load site_config
    bucket = Bucket.create!
    bucket.publish site
    {
      :site => site_config,
      :bucket => bucket.name,
      :url => bucket.url,
    }
  end
end
