require 'aws-sdk-resources'
require 'securerandom'
require 'mimemagic'

module S3rve
  class Bucket
    S3 = Aws::S3::Resource.new

    def self.create!
      bucket_name = new_bucket_name

      bucket = S3.create_bucket bucket: bucket_name

      return new bucket
    end

    def initialize(bucket)
      @bucket = bucket
    end

    def publish(site)
      website_configuration = site_configuration site
      @bucket.website.put website_configuration: website_configuration

      upload site
    end

    def name
      @bucket.name
    end

    def url
      "http://#{@bucket.name}.s3-website-#{region}.amazonaws.com"
    end

    def upload(site)
      walk site.document_root do |path|
        if path.file?
          mime_type = guess_mime(path)

          relative_path = path.relative_path_from(site.document_root)
          name = site.clean_path(relative_path.to_s)

          File.open(path, 'rb') do |file|
            @bucket.put_object(
              key: name,
              body: file,
              content_type: mime_type.to_s,
              acl: 'public-read',
            )
          end
        end
      end
    end

    private

    def self.new_bucket_name
      "s3rve-#{SecureRandom.uuid}"
    end

    def site_configuration(site)
      config = index_configuration(site)
      config.merge! error_configuration(site)
      config
    end

    def index_configuration(site)
      {
        index_document: {
          suffix: site.clean_path("index.html"),
        },
      }
    end

    def error_configuration(site)
      if site.error_page
        {
          error_document: {
            key: site.error_page
          }
        }
      else
        {}
      end
    end

    def region
      # Close enough!
      S3.client.config.region
    end

    def guess_mime(path)
      by_path = MimeMagic.by_path(path)
      if !by_path.nil?
        return by_path
      end

      File.open(path, 'rb') do |file|
        by_magic = MimeMagic.by_magic(file)
        if !by_magic.nil?
          return by_magic
        end
      end

      return MimeMagic.new 'application/octet-stream'
    end

    def walk(path, &block)
      path.each_child do |entry|
        block.call entry
        if entry.directory?
          walk entry, &block
        end
      end
    end
  end
end
