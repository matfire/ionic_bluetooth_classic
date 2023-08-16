import Foundation

@objc public class Bluetooth: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
