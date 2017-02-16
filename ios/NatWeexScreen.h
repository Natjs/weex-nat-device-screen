//
//  NatWeexScreen.h
//
//  Created by huangyake on 17/1/7.
//  Copyright © 2017 Nat. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <WeexSDK/WeexSDK.h>


@protocol NatWeexScreenPro <WXModuleProtocol>

- (void)info:(WXModuleCallback)callback;
- (void)getBrightness:(WXModuleCallback)callback;
- (void)setBrightness:(NSNumber *)brightness :(WXModuleCallback)callback;
- (void)getOrientation:(WXModuleCallback)callback;

@end

@interface NatWeexScreen : NSObject<NatWeexScreenPro>

@end
