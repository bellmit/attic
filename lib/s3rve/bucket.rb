require 'aws-sdk-resources'
require 'securerandom'
require 'mimemagic'

module S3rve
  class Bucket
    S3 = Aws::S3::Resource.new

    def self.create!
      bucket_name = new_bucket_name

      bucket = S3.create_bucket bucket: bucket_name
      bucket.website.put website_configuration: website_config

      return new bucket
    end

    def initialize(bucket)
      @bucket = bucket
    end

    def name
      @bucket.name
    end

    def url
      "http://#{@bucket.name}.s3-website-#{region}.amazonaws.com"
    end

    def upload(source_path)
      walk source_path do |path|
        if path.file?
          relative_path = path.relative_path_from source_path
          name = relative_path.to_s
          mime_type = guess_mime path

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

    def self.website_config
      {
        index_document: {
          suffix: "index.html",
        },
      }
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
