#!/usr/bin/env python3
import json
file_path = '/opt/aws/amazon-cloudwatch-agent/etc/amazon-cloudwatch-agent.d/file_beanstalk.json'
with open(file_path, 'r') as f:
    jj = json.load(f)
    collect_list = jj['logs']['logs_collected']['files']['collect_list']
    one_entity = collect_list[0]
    one_entity_log_group_name = one_entity['log_group_name']
    path = '/'.join(one_entity_log_group_name.split('/')[:-1])
    new_collect_list = [
        {
                            "file_path": "/var/log/spring.log",
                            "log_group_name": "/aws/elasticbeanstalk/Ggiriggiriserver-env/var/log/spring.log",
                            "log_stream_name": "{instance_id}",
                            "retention_in_days": 30
        },
    ]
    collect_list += new_collect_list
    new_cloudwatch_logs = {
        'logs': {
            'logs_collected': {
                'files': {
                    'collect_list': collect_list
                }
            }
        }
    }
with open(file_path, 'w') as f:
    json.dump(new_cloudwatch_logs, f)